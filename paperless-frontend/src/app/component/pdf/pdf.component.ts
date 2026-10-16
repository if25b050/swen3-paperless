import {ChangeDetectorRef, Component, inject, input} from '@angular/core';
import {DocumentService} from '../../service/document.service';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {DocumentResponse} from '../../dto/response/document-response';
import {toObservable} from '@angular/core/rxjs-interop';
import {debounceTime} from 'rxjs';

@Component({
  selector: 'app-pdf',
  imports: [],
  templateUrl: './pdf.component.html',
  styleUrl: './pdf.component.scss',
})
export class PdfComponent {

  private documentService = inject(DocumentService);
  private sanitizer = inject(DomSanitizer);
  private changeDetectorRef = inject(ChangeDetectorRef);

  public pdfUrl: SafeUrl | null = null;
  pdf = input.required<DocumentResponse | null>();

  constructor() {
    toObservable(this.pdf)
      .pipe(debounceTime(100))
      .subscribe(pdf => {
        this.loadPdf(pdf);
      })
  }

  public loadPdf(pdf: DocumentResponse | null) {
    if (pdf) {
      this.documentService.getDocumentFile(pdf.uuid).subscribe({
        next: file => {
          // TODO add Filename?
          const rawUrl = URL.createObjectURL(new File([file], pdf.name, {type: 'application/pdf'}));

          const fullViewerUrl = `${rawUrl}#view=FitH`;
          this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(fullViewerUrl);
          this.changeDetectorRef.detectChanges();
          //  TODO Inhaltstreffer
          // const searchUrl = `${rawUrl}#search=${encodeURIComponent(this.searchQuery)}`;
          // this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(searchUrl);

        }, error: error => {
          // TODO Error Handling
          console.error(error);
        }
      });
    } else {
      this.pdfUrl = null;
      this.changeDetectorRef.detectChanges();
    }
  }
}
