/*
 * Copyright 2013-2016 Sergey Ignatov, Alexander Zolotov, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goide.project;

import com.goide.sdk.GoSdkService;
import com.intellij.openapi.module.Module;
import com.intellij.util.ThreeState;
import com.intellij.util.text.VersionComparatorUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class GoVendoringUtil {
  public static boolean supportsVendoringByDefault(@Nullable String sdkVersion) {
    return VersionComparatorUtil.compare(sdkVersion, "1.6") >= 0;
  }

  public static boolean vendoringCanBeDisabled(@Nullable String sdkVersion) {
    return VersionComparatorUtil.compare(sdkVersion, "1.7") < 0;
  }

  public static boolean supportsInternalPackages(@Nullable String sdkVersion) {
    return VersionComparatorUtil.compare(sdkVersion, "1.5") >= 0;
  }

  public static boolean supportsSdkInternalPackages(@Nullable String sdkVersion) {
    return VersionComparatorUtil.compare(sdkVersion, "1.4") >= 0;
  }

  public static boolean supportsVendoring(@Nullable String sdkVersion) {
    return VersionComparatorUtil.compare(sdkVersion, "1.4") >= 0;
  }

  @Contract("null -> false")
  public static boolean isVendoringEnabled(@Nullable Module module) {
    if (module == null) {
      return false;
    }

    String version = GoSdkService.getInstance(module.getProject()).getSdkVersion(module);
    if (!vendoringCanBeDisabled(version)) {
      return true;
    }
    ThreeState vendorSupportEnabled = GoModuleSettings.getInstance(module).getVendoringEnabled();
    if (vendorSupportEnabled == ThreeState.UNSURE) {
      return supportsVendoring(version) && supportsVendoringByDefault(version);
    }
    return vendorSupportEnabled.toBoolean();
  }
}
