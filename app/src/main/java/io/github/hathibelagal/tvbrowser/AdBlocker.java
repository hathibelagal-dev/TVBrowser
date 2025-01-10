/* Copyright 2024 Ashraff Hathibelagal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hathibelagal.tvbrowser;

import android.content.Context;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;

public class AdBlocker {

    static String[] fastBlockHosts = null;

    @Nullable
    public static WebResourceResponse processRequest(Context context, WebResourceRequest request) {
        String hostname = request.getUrl().getHost();
        if(hostname == null) {
            return null;
        }
        boolean valid = true;
        if(fastBlockHosts == null) {
            fastBlockHosts = context.getResources().getStringArray(R.array.blocked);
        }
        for(String host: fastBlockHosts) {
            if(hostname.endsWith(host)) {
                valid = false;
                break;
            }
        }
        if (!valid) {
            return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
        }
        return null;
    }
}
