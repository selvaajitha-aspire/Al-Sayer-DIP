//Resources to be loaded for translations
import { en } from './translation-mapping/index-en';
import { ar } from './translation-mapping/index-ar';

interface TranslationResources {
  [lang: string]: {
    [chunkName: string]: {
      [key: string]: any;
    };
  };
}

export const customTranslationResources: TranslationResources = {
  en,
  ar
};
