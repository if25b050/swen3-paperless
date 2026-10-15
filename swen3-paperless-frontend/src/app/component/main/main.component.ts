import {ChangeDetectorRef, Component, inject, OnInit, signal, ViewChild} from '@angular/core';
import {DocumentService} from '../../service/document.service';
import {LabelService} from '../../service/label.service';
import {SearchService} from '../../service/search.service';
import {ActivatedRoute, Router} from '@angular/router';
import {DocumentResponse} from '../../dto/response/document-response';
import {SearchResult} from '../../dto/search-result';
import {PdfComponent} from '../pdf/pdf.component';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {LabelOption} from '../../dto/label-option';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatDialog} from '@angular/material/dialog';


@Component({
  selector: 'app-main',
  imports: [
    PdfComponent,
    FormsModule,
    CommonModule
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
  standalone: true,
})
export class MainComponent implements OnInit {

  private cdr = inject(ChangeDetectorRef);
  private documentService = inject(DocumentService);
  private labelService = inject(LabelService);
  private searchService = inject(SearchService);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private snackbar = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  @ViewChild(PdfComponent)
  private pdfComponent!: PdfComponent;

  currentFiles = signal<DocumentResponse[]>([]);
  selectedFile = signal<DocumentResponse | null>(null);

  searchQuery = '';
  searchResults: SearchResult[] = [];

  labelSearchQuery = '';
  isLabelDropdownOpen = false;
  labels: LabelOption[] = []; // Keine Labels zu Beginn

  ngOnInit(): void {
    this.reloadDocuments();
    this.loadAllLabels();
  }

  private loadAllLabels(): void {
    this.labelService.getLabels().subscribe({
      next: (response) => {
        this.labels = response.map(label => ({
          ...label,
          selected: false
        }));
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('Fehler beim Laden der Labels:', err);
      }
    });
  }

  // Getter für gefilterte Labels beim Tippen
  get filteredLabels(): LabelOption[] {
    if (!this.labelSearchQuery.trim()) return [];
    return this.labels.filter(label =>
      label.name.toLowerCase().includes(this.labelSearchQuery.toLowerCase())
    );
  }

  // Wird beim Tippen in der Label-Searchbar ausgeführt
  onLabelSearchInput(): void {
    this.isLabelDropdownOpen = this.labelSearchQuery.trim().length > 0;
  }

  // Enter drücken: Label wird erstellt und ausgewählt

  createAndSelectLabel(): void {
    const selectedFile = this.selectedFile();
    if (!selectedFile) return;

    const trimmedValue = this.labelSearchQuery.trim();
    if (!trimmedValue) return;

    if (!selectedFile.labels) {
      selectedFile.labels = [];
    }

    // Prüfen, ob das Label bereits am Dokument hängt
    const existingDocLabel = selectedFile.labels.find(
      l => l.name.toLowerCase() === trimmedValue.toLowerCase()
    );

    if (existingDocLabel) {
      this.labelSearchQuery = '';
      this.isLabelDropdownOpen = false;
      return;
    }

    // Prüfen, ob das Label bereits global existiert
    const existingGlobalLabel = this.labels.find(
      l => l.name.toLowerCase() === trimmedValue.toLowerCase()
    );

    if (existingGlobalLabel) {
      this.assignLabelToDocument(
        selectedFile.uuid,
        existingGlobalLabel.uuid
      );
    } else {
      // Nur das neue Label erstellen und hinzufügen
      this.labelService.createLabel(trimmedValue).subscribe({
        next: (createdLabel) => {
          const newLabelOption: LabelOption = {
            uuid: createdLabel.uuid,
            name: createdLabel.name,
            selected: true
          };

          this.snackbar.open("Ein neues Label wurde erstellt.", "OK", {duration: 2500});

          this.labels.push(newLabelOption);

          this.assignLabelToDocument(
            selectedFile.uuid,
            createdLabel.uuid
          );
        },
        error: (err) => {
          console.error('Fehler beim Erstellen des Labels:', err);
        }
      });
    }

    this.labelSearchQuery = '';
    this.isLabelDropdownOpen = false;
  }

  getUniqueLabels() {
    const labels = this.selectedFile()?.labels ?? [];

    return labels.filter(
      (label, index, self) =>
        self.findIndex(l => l.uuid === label.uuid) === index
    );
  }

  private assignLabelToDocument(documentUuid: string, labelUuid: string): void {
    const file = this.selectedFile();

    if (file) {
      if (!file.labels) {
        file.labels = [];
      }

      if (!file.labels.some(l => l.uuid === labelUuid)) {
        const addedLabel = this.labels.find(l => l.uuid === labelUuid);
        if (addedLabel) {
          file.labels.push({
            uuid: addedLabel.uuid,
            name: addedLabel.name
          });

          const labelIds: string[] = file.labels.map(l => l.uuid);

          console.log('Labels vor dem Speichern:', file.labels);
          console.log('Label-UUIDs:', labelIds);
          this.documentService.updateDocument(file.uuid, {
            name: file.name,
            labels: labelIds
          }).subscribe({
            next: () => {
              this.cdr.detectChanges()
            },
            error: err => console.log('Fehler beim Speichern der Dokument-Labels:', err)
          });
        }
      }
      this.cdr.detectChanges();
    }
  }

  toggleLabelSelection(label: LabelOption): void {
    const selectedFile = this.selectedFile();
    if (!selectedFile) return;

    // Ensure labels array exists on the file
    if (!selectedFile.labels) {
      selectedFile.labels = [];
    }

    const index = selectedFile.labels.findIndex(l => l.uuid === label.uuid);

    if (index > -1) {
      // Remove label if it already exists on this document
      selectedFile.labels.splice(index, 1);
      label.selected = false;
    } else {
      // Add label if it doesn't exist on this document
      selectedFile.labels.push({uuid: label.uuid, name: label.name});
      label.selected = true;
    }

    // Extract just the UUIDs for the backend DTO
    const labelIds = selectedFile.labels.map(l => l.uuid);

    this.documentService.updateDocument(selectedFile.uuid, {
      name: selectedFile.name,
      labels: labelIds
    }).subscribe({
      next: () => this.cdr.detectChanges(),
      error: err => console.log('Fehler beim Aktualisieren der Labels:', err)
    });
  }

  private reloadDocuments(): void {
    this.documentService.getDocuments().subscribe({
      next: response => {
        this.currentFiles.set(response);

        let uuid = this.activatedRoute.snapshot.paramMap.get("uuid");
        if (uuid) {
          const file = this.currentFiles().find(file => file.uuid === uuid)
          if (file) {
            this.selectedFile.set(file);

            const docLabelUuids = file.labels?.map(l => l.uuid) || [];
            this.labels.forEach(label => {
              label.selected = docLabelUuids.includes(label.uuid);
            });
          }
        }
        if (!this.selectedFile()) {
          this.router.navigate(['/']);
        }
      }, error: err => {
        // Error Handling
        console.log(err);
      }
    });
  }

  onFileUpload(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      // Überprüfung, ob es sich wirklich um eine PDF-Datei handelt
      if (file.type !== 'application/pdf') {
        alert('Bitte nur PDF-Dateien hochladen!');
        input.value = '';
        return;
      }

      const selectedFile = this.selectedFile();
      if (selectedFile) {
        this.documentService.updateDocumentFile(selectedFile.uuid, file).subscribe({
          next: response => {
            this.snackbar.open("Das Dokument wurde geupdatet.", "OK", {duration: 2500});
            this.pdfComponent.loadPdf(selectedFile)
          },
          error: err => {
            // TODO Error Handling
            console.log(err);
          }
        })
      } else {
        this.documentService.createDocument(file).subscribe({
          next: document => {
            // Automatisch die neu hochgeladene Datei auswählen
            this.selectFile(document);
            this.snackbar.open("Das neue Dokument wurde erstellt.", "OK", {duration: 2500});

            // Input zurücksetzen, damit dieselbe Datei erneut gewählt werden kann
            input.value = '';
            this.cdr.detectChanges();

            // Dokumente neu laden, damit sie angezeigt werden
            this.reloadDocuments();
          },
          error: err => {
            // TODO Error Handling
            console.log(err);
          }
        });
      }
    }
  }

  // Ausführen der Suche bei Texteingabe
  onSearchInput(event: Event): void {
    const query = (event.target as HTMLInputElement).value;
    this.searchQuery = query;

    if (!query.trim()) {
      this.searchResults = [];
      return;
    }

    const term = query.toLowerCase();
    const results: SearchResult[] = [];


    this.searchService.searchDocuments({search: term, page: 1, pageSize: 10}).subscribe({
      next: response => {
        response.list.forEach((doc: DocumentResponse) => {
          results.push({
            document: doc,
            type: 'filename'
          });
        })

        // TODO Backend-Kompatibel einbauen
        // Suche im Inhalt der PDF
        // const contentIndex = fileItem.extractedText.toLowerCase().indexOf(term);
        // if (contentIndex !== -1) {
        //   // Erstelle Snippet um den Suchbegriff
        //   const start = Math.max(0, contentIndex - 30);
        //   const end = Math.min(fileItem.extractedText.length, contentIndex + term.length + 30)
        //   const snippet = fileItem.extractedText.substring(start, end);
        //
        //   results.push({
        //     id: fileItem.file.name + '-content',
        //     type: 'content',
        //     fileItem,
        //     snippet
        //   });
        // }

        this.searchResults = results;
        this.cdr.detectChanges();
      },
      error: error => {
        // TODO Error Handling
        console.log(error);
        this.searchResults = [];
      }
    });
  }

  selectFile(document: DocumentResponse): void {
    this.selectedFile.set(document);
    this.searchResults = [];

    const docLabelUuids = document.labels?.map(l => l.uuid) || [];
    this.labels.forEach(label => {
      label.selected = docLabelUuids.includes(label.uuid);
    });

    this.router.navigate(['pdf', document.uuid]);
    this.cdr.detectChanges();
  }

  deleteSelectedFile(): void {
    const selectedFile = this.selectedFile();
    if (!selectedFile) return;

    this.documentService.deleteDocument(selectedFile.uuid).subscribe({
      next: response => {
        this.snackbar.open("Das Dokument wurde gelöscht.", "OK", {duration: 2500});
        this.reloadDocuments();
      },
      error: err => {
        console.log(err);
        // TODO Error Handling
      }
    })

    // Ausgewählte Datei zurücksetzen
    this.selectedFile.set(null);
  }
}
