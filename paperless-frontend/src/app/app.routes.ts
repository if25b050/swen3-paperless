import {Routes} from '@angular/router';
import {MainComponent} from './component/main/main.component';

export const routes: Routes = [
  {path: '', component: MainComponent},
  {path: "pdf/:uuid", component: MainComponent}
];
