import {
  Component,
  OnInit
} from '@angular/core';

import { AppState } from '../../app.service';
import { Title } from './title';
import { XLargeDirective } from './x-large';

@Component({
  selector: 'users',
  providers: [

  ],
  styleUrls: [ './users.component.css' ],
  templateUrl: './users.component.html'
})
export class UsersComponent implements OnInit {
  /**
   * Set our default values
   */
  public localState = { value: '' };
  /**
   * TypeScript public modifiers
   */
  constructor(
    public appState: AppState,
  ) {}

  public ngOnInit() {
    console.log('hello `Home` component');
    /**
     * this.title.getData().subscribe(data => this.data = data);
     */
  }

  public submitState(value: string) {
    console.log('submitState', value);
    this.appState.set('value', value);
    this.localState.value = '';
  }
}
