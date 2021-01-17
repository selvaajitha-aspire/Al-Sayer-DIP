import { StateConfig, StorageSyncType } from "@spartacus/core";

export function refreshTokenConfigFactory(): StateConfig {
    const config: StateConfig = {
      state: {
        storageSync: {
          keys: {
            "auth.userToken.token.refresh_token": StorageSyncType.SESSION_STORAGE
          }
        }
      }
    };
    return config;
  }