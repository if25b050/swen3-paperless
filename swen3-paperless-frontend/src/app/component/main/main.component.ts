import {ChangeDetectorRef, Component, inject, OnInit, signal, ViewChild} from '@angular/core';
import {DocumentService} from '../../service/document.service';
import {LabelService} from '../../service/label.service';
import {SearchService} from '../../service/search.service';
import {ActivatedRoute, Router} from '@angular/router';
import {DocumentResponse} from '../../dto/response/document-response';
import {SearchResult} from '../../dto/search-result';
import {PdfComponent} from '../pdf/pdf.component';

@Component({
  selector: 'app-main',
  imports: [
    PdfComponent
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
  @ViewChild(PdfComponent)
  private pdfComponent!: PdfComponent;

  currentFiles = signal<DocumentResponse[]>([]);
  selectedFile = signal<DocumentResponse | null>(null);

  searchQuery = '';
  searchResults: SearchResult[] = [];

  ngOnInit(): void {
    this.reloadDocuments();
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
            // TODO Handle Success
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

            // Input zurücksetzen, damit dieselbe Datei erneut gewählt werden kann
            input.value = '';
            this.cdr.detectChanges();
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
    this.router.navigate(['pdf', document.uuid]);
  }

  deleteSelectedFile(): void {
    const selectedFile = this.selectedFile();
    if (!selectedFile) return;

    this.documentService.deleteDocument(selectedFile.uuid).subscribe({
      next: response => {
        // TODO Success Handling
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
