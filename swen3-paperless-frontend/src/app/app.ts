import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import * as pdfjsLib from 'pdfjs-dist'

// Worker für PDF.js definieren
pdfjsLib.GlobalWorkerOptions.workerSrc = 'assets/pdf.worker.min.mjs';

export interface PdfItem {
  file: File;
  safeUrl: SafeResourceUrl;
  extractedText: string; // Speichert Text dem PDF
}

export interface SearchResult {
  id: string;
  type: 'filename' | 'content';
  fileItem: PdfItem;
  snippet?: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class AppComponent {
  private sanitizer = inject(DomSanitizer);
  private cdr = inject(ChangeDetectorRef);

  uploadedFiles: PdfItem[] = [];
  selectedFile: PdfItem | null = null;

  searchQuery = '';
  searchResults: SearchResult[] = [];

  async onFileSelected(event: Event): Promise<void> {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      // Überprüfung, ob es sich wirklich um eine PDF-Datei handelt
      if (file.type !== 'application/pdf') {
        alert('Bitte nur PDF-Dateien hochladen!');
        input.value='';
        return;
      }

      // Duplikatsprüfung anhand des Dateinamens
      const isDuplicate = this.uploadedFiles.some(
        item => item.file.name === file.name
      );

      if(isDuplicate) {
        alert(`Die Datei "${file.name}" wurde bereits hochgeladen!`);
        input.value = '';
        return;
      }

      // Text aus PDF extrahieren
      const extractedText = await this.extractTextFromPdf(file);

      // Erstelle eine Vorschau-URL für die lokale Datei
      const objectUrl = URL.createObjectURL(file);
      const safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(`${objectUrl}#view=Fit`);
      const newItem: PdfItem = {file, safeUrl, extractedText};
      this.uploadedFiles.push(newItem);

      // Automatisch die neu hochgeladene Datei auswählen
      this.selectFile(newItem)

      // Input zurücksetzen, damit dieselbe Datei erneut gewählt werden kann
      input.value = '';

      this.cdr.detectChanges();
    }
  }

  // Extrahiert den reinen Text aus allen Seiten einzelner PDF
  private async extractTextFromPdf(file: File): Promise<string> {
    try {
      const arrayBuffer = await file.arrayBuffer();
      const pdf = await pdfjsLib.getDocument({data: arrayBuffer}).promise;
      let fullText = '';

      for(let i = 1; i <= pdf.numPages; i++) {
        const page = await pdf.getPage(i);
        const textContent = await page.getTextContent();
        const pageText = textContent.items.map((item: any) => item.str || '').join(' ');        fullText += pageText + ' ';
      }

      return fullText;
    } catch(e) {
      console.error('Fehler beim Extrahieren des PDF-Textes:', e);
      return '';
    }
  }

  // Ausführen der Suche bei Texteingabe
  onSearchInput(event: Event): void {
    const query = (event.target as HTMLInputElement).value;
    this.searchQuery = query;

    if(!query.trim()) {
      this.searchResults = [];
      return;
    }

    const term = query.toLowerCase();
    const results: SearchResult[] =[];

    this.uploadedFiles.forEach(fileItem => {
      // Suche in Dateinamen
      if(fileItem.file.name.toLowerCase().includes(term)) {
        results.push({
          id: fileItem.file.name+'-name',
          type: 'filename',
          fileItem
        });
      }

      // Suche im Inhalt der PDF
      const contentIndex = fileItem.extractedText.toLowerCase().indexOf(term);
      if(contentIndex !== -1) {
        // Erstelle Snippet um den Suchbegriff
        const start = Math.max(0, contentIndex - 30);
        const end = Math.min(fileItem.extractedText.length, contentIndex + term.length+30)
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
  openSearchResult(result: SearchResult): void{
    const rawUrl = URL.createObjectURL(result.fileItem.file);

    if(result.type === 'filename') {
      const fullViewerUrl = `${rawUrl}#view=FitH`;
      result.fileItem.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(fullViewerUrl);
    } else {
      // Inhaltstreffer
      const searchUrl = `${rawUrl}#search=${encodeURIComponent(this.searchQuery)}`;
      result.fileItem.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(searchUrl);
    }

    this.selectFile(result.fileItem);
    this.searchResults = []; // Dropdown schließen
  }

  selectFile(fileItem: PdfItem): void {
    this.selectedFile = fileItem;
  }

  deleteSelectedFile(): void {
    if (!this.selectedFile) return;

    // Aus Liste entfernen
    this.uploadedFiles = this.uploadedFiles.filter(item => item !== this.selectedFile)

    // Ausgewählte Datei zurücksetzen
    this.selectedFile = null;
  }
}

export default AppComponent;
