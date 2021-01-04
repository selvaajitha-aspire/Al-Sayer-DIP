import { UpdateProfileModule } from './update-profile/update-profile.module';
import { MyVehiclesModule } from './my-vehicles/my-vehicles.module';
import { RegisterModule } from './register/register.module';
import { LoginFormModule } from './login-form/login-form.module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { B2cStorefrontModule, DirectionMode, DirectionConfig, LayoutConfig, PageSlotModule, NavigationModule, HamburgerMenuModule, IconModule, GenericLinkModule, SiteContextSelectorModule } from '@spartacus/storefront';
import { OccConfig, ConfigModule, I18nModule, StoreFinderConfig } from '@spartacus/core';
import { environment } from './../environments/environment';
import { RoadsideAssistanceModule } from './roadside-assistance/roadside-assistance.module';

import { HomePageModule } from './home-page-components/home-page.module';
import { HeaderMenuComponent } from './home-page-components/header/header-menu/header-menu.component';
import { SigninMenuComponent } from './home-page-components/header/signin-menu/signin-menu.component';
import { MyTicketsModule } from './my-tickets/my-tickets.module';
import { FooterComponent } from './home-page-components/footer/footer.component';
import { AlsayerNavigationComponent } from './home-page-components/alsayer-navigation/alsayer-navigation.component';
import { ServiceHistoryModule } from './service-history/service-history.module';

import { customTranslationResources } from './internationalization/custom-translation-resources';
import { customTranslationConfig } from './internationalization/translation-config/custom-translation-config';

import { MarkAsteriskDirectiveModule } from './directives/mark-asterisk.directive';
import { InsuranceModule } from './insurance/insurance.module';



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
    AppComponent,
    SigninMenuComponent,
    HeaderMenuComponent,
    FooterComponent,
    AlsayerNavigationComponent,
  ],
  imports: [
    BrowserModule,
    HomePageModule,
    PageSlotModule,
    NavigationModule,
    HamburgerMenuModule,
    BrowserAnimationsModule,
    RouterModule,
    IconModule,
    GenericLinkModule,
    I18nModule,
    SiteContextSelectorModule,
    ToastrModule.forRoot(),
    B2cStorefrontModule.withConfig({
      backend: occConfig.backend,
      context: {
        currency: ['KWD'],
        language: ['en','ar'],
        baseSite: ['alsayer-spa'],
        urlParameters: ['language']
      },
      i18n: {
        resources: customTranslationResources,
        chunks: customTranslationConfig,
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
        rtlLanguages: ['ar']
      },
    } as DirectionConfig),
    ConfigModule.withConfig({
      layoutSlots: {
        LandingPage2Template: {
          slots: [
            'Section1',
            'ServiceTileSection',
            'Section2A',
            'Section2B',
            'Section2C',
            'Section3',
            'Section4',
            'Section5',
          ],
        }
      }
    } as LayoutConfig),
    ConfigModule.withConfig({
      googleMaps: {
        apiUrl: 'https://maps.googleapis.com/maps/api/js',
        apiKey: 'AIzaSyDQzDMu8IB2-gRYDpq8cA4GAqsIF3q0d_o',
        scale: 15,
        selectedMarkerScale: 17,
        radius: 50000,
     }
    } as StoreFinderConfig),
    RoadsideAssistanceModule,
    RegisterModule,
    LoginFormModule,
    MyVehiclesModule,
    MyTicketsModule,
    UpdateProfileModule,
    ServiceHistoryModule,
    MarkAsteriskDirectiveModule,
    InsuranceModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
