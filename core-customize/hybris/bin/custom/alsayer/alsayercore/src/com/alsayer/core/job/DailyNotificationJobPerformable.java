package com.alsayer.core.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class DailyNotificationJobPerformable extends AbstractJobPerformable<CronJobModel> {
    @Override
    public PerformResult perform(CronJobModel cronJobModel) {
        System.out.println("Cron Job is Working");
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }
}
