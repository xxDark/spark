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
package spark.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Serializer that writes the result of toString to output in UTF-8 encoding
 *
 * @author alex
 */
final class DefaultSerializer extends Serializer {

    @Override
    public boolean canProcess(Object element) {
        return true;
    }

    @Override
    public void process(OutputStream outputStream, Object element) throws IOException {
        outputStream.write(element.toString().getBytes(StandardCharsets.UTF_8));
    }

}
