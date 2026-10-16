import {Component, inject, OnInit} from '@angular/core';
import {MatInput, MatInputModule} from '@angular/material/input';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';
import {FormsModule} from '@angular/forms';
import {DocumentService} from '../../service/document.service';
import {DocumentResponse} from '../../dto/response/document-response';
import {MatFormFieldModule} from '@angular/material/form-field';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-edit-document',
  imports: [
    MatInput,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButton,
    FormsModule,
    MatDialogClose,
    MatFormFieldModule,
    MatInputModule,
    CommonModule,
  ],
  templateUrl: './edit-document.dialog.html',
  styleUrl: './edit-document.dialog.scss',
})
export class EditDocumentDialog implements OnInit {

  public name: string = "";
  private data: DocumentResponse = inject(MAT_DIALOG_DATA);
  private dialogRef = inject(MatDialogRef);
  private documentService = inject(DocumentService);

  public ngOnInit() {
    this.name = this.data.name.substring(0, this.data.name.length - ".pdf".length);
  }

  public save(): void {
    this.documentService.updateDocument(this.data.uuid, {
      name: this.name + ".pdf",
      labels: this.data.labels.map(l => l.uuid)
    }).subscribe({
      next: (result) => {
        this.dialogRef.close(result);
      },
      error: err => {
        console.error(err);
      }
    })
  }

  protected readonly JSON = JSON;
}
