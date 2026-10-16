import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PdfComponent} from './pdf.component';
import {inputBinding, signal} from '@angular/core';

describe('PDFComponent', () => {
  let component: PdfComponent;
  let fixture: ComponentFixture<PdfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PdfComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PdfComponent,
      {
        bindings: [
          inputBinding('pdf', signal(null))
        ]
      });
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
