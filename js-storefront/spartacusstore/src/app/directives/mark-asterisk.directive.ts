import { Directive, Renderer2, ElementRef, OnInit, NgModule } from '@angular/core';

@Directive({
  selector: '[required]'
})
export class MarkAsteriskDirective implements OnInit {

  constructor(
    private renderer: Renderer2, 
    private el: ElementRef
  ) { }

ngOnInit(){
  const parent = this.renderer.parentNode(this.el.nativeElement);

    if (parent.getElementsByTagName('LABEL').length && !parent.getElementsByClassName('required-asterisk').length) {
      parent.getElementsByTagName('LABEL')[0].innerHTML += '<sup><span class="required-asterisk">*</span></sup>';
    }
}

}

@NgModule({
  declarations: [ MarkAsteriskDirective ],
  exports: [ MarkAsteriskDirective ]
})
export class MarkAsteriskDirectiveModule {}