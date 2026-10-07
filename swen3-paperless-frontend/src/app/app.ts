import {ChangeDetectorRef, Component, inject} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';
import * as pdfjsLib from 'pdfjs-dist'
import {PdfItem} from './dto/pdf-item';
import {SearchResult} from './dto/search-result';
import {DocumentService} from './service/document.service';
import {LabelService} from './service/label.service';
import {SearchService} from './service/search.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class AppComponent {
  private sanitizer = inject(DomSanitizer);
  private cdr = inject(ChangeDetectorRef);
  private documentService = inject(DocumentService);
  private labelService = inject(LabelService);
  private searchService = inject(SearchService);
  private router = inject(Router);

  currentFiles: PdfItem[] = [];
  selectedFile: PdfItem | null = null;

  searchQuery = '';
  searchResults: SearchResult[] = [];

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

      this.documentService.createDocument(file).subscribe(document => {
        // Automatisch die neu hochgeladene Datei auswählen
        this.selectFile(document.uuid);

        // Input zurücksetzen, damit dieselbe Datei erneut gewählt werden kann
        input.value = '';
        this.cdr.detectChanges();
      });
    }
  }

  // Extrahiert den reinen Text aus allen Seiten einzelner PDF
  private async extractTextFromPdf(file: File): Promise<string> {
    try {
      const arrayBuffer = await file.arrayBuffer();
      const pdf = await pdfjsLib.getDocument({data: arrayBuffer}).promise;
      let fullText = '';

      for (let i = 1; i <= pdf.numPages; i++) {
        const page = await pdf.getPage(i);
        const textContent = await page.getTextContent();
        const pageText = textContent.items.map((item: any) => item.str || '').join(' ');
        fullText += pageText + ' ';
      }

      return fullText;
    } catch (e) {
      console.error('Fehler beim Extrahieren des PDF-Textes:', e);
      return '';
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

    this.currentFiles.forEach(fileItem => {
      // Suche in Dateinamen
      if (fileItem.file.name.toLowerCase().includes(term)) {
        results.push({
          id: fileItem.file.name + '-name',
          type: 'filename',
          fileItem
        });
      }

      // Suche im Inhalt der PDF
      const contentIndex = fileItem.extractedText.toLowerCase().indexOf(term);
      if (contentIndex !== -1) {
        // Erstelle Snippet um den Suchbegriff
        const start = Math.max(0, contentIndex - 30);
        const end = Math.min(fileItem.extractedText.length, contentIndex + term.length + 30)
        const snippet = fileItem.extractedText.substring(start, end);

        results.push({
          id: fileItem.file.name + '-content',
          type: 'content',
          fileItem,
          snippet
        });
      }
    });

    this.searchResults = results;
  }

  // Öffnet das PDF und übergibt den Parameter für die Textmarkierung
  openSearchResult(result: SearchResult): void {
    const rawUrl = URL.createObjectURL(result.fileItem.file);

    if (result.type === 'filename') {
      const fullViewerUrl = `${rawUrl}#view=FitH`;
      result.fileItem.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(fullViewerUrl);
    } else {
      // Inhaltstreffer
      const searchUrl = `${rawUrl}#search=${encodeURIComponent(this.searchQuery)}`;
      result.fileItem.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(searchUrl);
    }

    this.selectFile(result.id);
    this.searchResults = []; // Dropdown schließen
  }

  selectFile(newUuid: string): void {
    this.router.navigate([newUuid]);
  }

  deleteSelectedFile(): void {
    if (!this.selectedFile) return;

    // Aus Liste entfernen
    this.currentFiles = this.currentFiles.filter(item => item !== this.selectedFile)

    // Ausgewählte Datei zurücksetzen
    this.selectedFile = null;
  }
}

export default AppComponent;
