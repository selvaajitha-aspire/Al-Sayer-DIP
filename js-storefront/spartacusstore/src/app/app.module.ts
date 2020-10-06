import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { translations, translationChunksConfig } from '@spartacus/assets';
import { B2cStorefrontModule, DirectionMode, DirectionConfig } from '@spartacus/storefront';
import { OccConfig, ConfigModule } from '@spartacus/core';
import { environment } from './../environments/environment';
import { RoadsideAssistanceModule } from './roadside-assistance/roadside-assistance.module';

const occConfig: OccConfig = { backend: { occ: {} } };

// only provide the `occ.baseUrl` key if it is explicitly configured, otherwise the value of
// <meta name="occ-backend-base-url" > is ignored.
// This in turn breaks the call to the API aspect in public cloud environments
if (environment.occBaseUrl) {
  occConfig.backend.occ.baseUrl = environment.occBaseUrl;
}
if (environment.prefix) {
  occConfig.backend.occ.prefix = environment.prefix;
}
else {
  occConfig.backend.occ.prefix = '/occ/v2/';
}


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    B2cStorefrontModule.withConfig({
      backend: occConfig.backend,
      context: {
        currency: ['KWD'],
        language: ['en'],
        baseSite: ['alsayer-spa']
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
      direction: {
          default: DirectionMode.LTR,
          ltrLanguages: [],
      },
    } as DirectionConfig),
    RoadsideAssistanceModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
