import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HeaderTitleService {

  constructor() { }

  public headerTitle = new Subject();

}
