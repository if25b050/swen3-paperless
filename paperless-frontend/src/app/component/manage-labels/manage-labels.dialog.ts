import {Component, inject, signal} from '@angular/core';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatDialog, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from '@angular/material/dialog';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatList, MatListItem} from '@angular/material/list';
import {LabelService} from '../../service/label.service';
import {LabelResponse} from '../../dto/response/label-response';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatIcon} from '@angular/material/icon';
import {ConfirmDialog} from '../confirm/confirm.dialog';

@Component({
  selector: 'app-manage-labels.dialog',
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatListItem,
    MatList,
    MatIconButton,
    MatIcon,
    FormsModule,
    MatDialogClose
  ],
  templateUrl: './manage-labels.dialog.html',
  styleUrl: './manage-labels.dialog.scss',
})
export class ManageLabelsDialog {
  private dialog = inject(MatDialog);
  private labelService = inject(LabelService);
  private snackbar = inject(MatSnackBar);

  public labels = signal<LabelResponse[]>([]);
  public currentlyEditedLabel: LabelResponse | null = null;
  public inputName: string = "";

  constructor() {
    this.labelService.getLabels().subscribe({
      next: labels => {
        this.labels.set(labels);
      },
      error: err => {
        console.error(err);
        this.snackbar.open("Labels konnten nicht geladen werden.", 'Ok');
      }
    });
  }

  public deleteLabel(label: LabelResponse) {
    this.dialog.open(ConfirmDialog, {data: "Wollen Sie wirklich das Label \"" + label.name + "\" löschen?"})
      .afterClosed().subscribe(result => {
      if (result) {
        this.labelService.deleteLabel(label.uuid).subscribe({
          next: () => {
            this.labels.set(this.labels().filter(l => l.uuid !== label.uuid));
          },
          error: err => {
            console.error(err);
            this.snackbar.open("Das Label konnte nicht gelöscht werden.", 'Ok');
          }
        });
      }
    });
  }

  public updateLabel(labelUuid: string | undefined, newLabel: string) {

    if (labelUuid === undefined) {
      this.labelService.createLabel(newLabel).subscribe({
        next: (label) => {
          // get labels
          const labels: LabelResponse[] = JSON.parse(JSON.stringify(this.labels()));
          // add label
          labels.push(label);
          // set new labels in the model
          this.labels.set(labels);
          this.currentlyEditedLabel = null;
        },
        error: err => {
          console.error(err);
          this.snackbar.open("Das Label konnte nicht erstellt werden.", 'Ok');
        }
      });
    } else {
      this.labelService.updateLabel(labelUuid, newLabel).subscribe({
        next: () => {
          // get labels
          const labels: LabelResponse[] = JSON.parse(JSON.stringify(this.labels()));
          // update labels
          labels.filter(l => l.uuid === labelUuid).forEach((l) => l.name = newLabel);
          // set new labels in the model
          this.labels.set(labels);
          this.currentlyEditedLabel = null;
        },
        error: err => {
          console.error(err);
          this.snackbar.open("Das Label konnte nicht geändert werden.", 'Ok');
        }
      });
    }
  }
}
