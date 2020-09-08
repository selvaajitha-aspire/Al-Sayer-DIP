import { BrowserModule, BrowserTransferStateModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { translations, translationChunksConfig } from '@spartacus/assets';
import { B2cStorefrontModule, PWAModuleConfig, DirectionMode, DirectionConfig } from '@spartacus/storefront';
import { ConfigModule } from '@spartacus/core';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'serverApp' }),
    AppRoutingModule,
    B2cStorefrontModule.withConfig({
      backend: {
        occ: {
          baseUrl: 'https://localhost:9002',
          prefix: '/occ/v2/'
        }
      },
      context: {
        currency: ['USD'],
        language: ['en'],
        baseSite: ['electronics-spa']
      },
      i18n: {
        resources: translations,
        chunks: translationChunksConfig,
        fallbackLang: 'en'
      },
      features: {
        level: '2.1'
      }
    }),
    ConfigModule.withConfig({
      pwa: {
        enabled: true,
        addToHomeScreen: true,
      }
    }as PWAModuleConfig),
    ConfigModule.withConfig({
      direction: {
          default: DirectionMode.LTR,
          rtlLanguages: [],
      },
    } as DirectionConfig),
    BrowserTransferStateModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
