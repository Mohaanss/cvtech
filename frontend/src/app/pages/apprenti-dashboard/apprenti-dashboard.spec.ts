import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprentiDashboard } from './apprenti-dashboard';

describe('ApprentiDashboard', () => {
  let component: ApprentiDashboard;
  let fixture: ComponentFixture<ApprentiDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApprentiDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApprentiDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
