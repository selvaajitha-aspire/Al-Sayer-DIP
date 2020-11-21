import { CmsBannerComponent, CmsComponent } from '@spartacus/core';
import { HomepageBannerComponent } from './brand.model';

export interface HomepageBannerCollectionComponent extends CmsComponent{
    components?: HomepageBannerComponent[];
}

