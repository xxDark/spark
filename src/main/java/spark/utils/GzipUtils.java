/*
 * Copyright 2015 - Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spark.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * GZIP utility class.
 *
 * @author Edward Raff
 * @author Per Wendel
 */
public class GzipUtils {

    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String CONTENT_ENCODING = "Content-Encoding";

    private static final String GZIP = "gzip";

    private static final StringMatch STRING_MATCH = new StringMatch();

    // Hide constructor
    private GzipUtils() {

    }

    /**
     * Checks if the HTTP request/response accepts and wants GZIP and i that case wraps the response output stream in a
     * {@link java.util.zip.GZIPOutputStream}.
     *
     * @param httpRequest        the HTTP servlet request.
     * @param httpResponse       the HTTP servlet response.
     * @param requireWantsHeader if wants header is required
     * @return if accepted and wanted a {@link java.util.zip.GZIPOutputStream} otherwise the unchanged response
     * output stream.
     * @throws IOException in case of IO error.
     */
    public static OutputStream checkAndWrap(HttpServletRequest httpRequest,
                                            HttpServletResponse httpResponse,
                                            boolean requireWantsHeader) throws
                                                                        IOException {
        OutputStream responseStream = httpResponse.getOutputStream();

        // GZIP Support handled here. First we must ensure that we want to use gzip, and that the client supports gzip
        boolean acceptsGzip = Collections.list(httpRequest.getHeaders(ACCEPT_ENCODING)).stream().anyMatch(STRING_MATCH);

        if (acceptsGzip) {
            boolean wantGzip = httpResponse.getHeaders(CONTENT_ENCODING).contains(GZIP);
            if (!requireWantsHeader || wantGzip) {
                responseStream = new GZIPOutputStream(responseStream, true);
                addContentEncodingHeaderIfMissing(httpResponse, wantGzip);
            }
        }

        return responseStream;
    }

    private static void addContentEncodingHeaderIfMissing(HttpServletResponse response, boolean wantsGzip) {
        if (!wantsGzip) {
            response.setHeader(CONTENT_ENCODING, GZIP);
        }
    }

    /**
     * Used instead of lambdas due to risk for java.lang.IncompatibleClassChangeError.
     */
    private static class StringMatch implements Predicate<String> {
        @Override
        public boolean test(String s) {
            return s != null && s.contains(GZIP);
        }
    }

}
