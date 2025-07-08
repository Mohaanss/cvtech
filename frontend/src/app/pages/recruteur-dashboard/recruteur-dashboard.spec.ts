import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruteurDashboard } from './recruteur-dashboard';

describe('RecruteurDashboard', () => {
  let component: RecruteurDashboard;
  let fixture: ComponentFixture<RecruteurDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecruteurDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecruteurDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
