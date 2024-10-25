import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntrygroupCardComponent } from './entrygroup-card.component';

describe('EntrygroupCardComponent', () => {
  let component: EntrygroupCardComponent;
  let fixture: ComponentFixture<EntrygroupCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EntrygroupCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EntrygroupCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
