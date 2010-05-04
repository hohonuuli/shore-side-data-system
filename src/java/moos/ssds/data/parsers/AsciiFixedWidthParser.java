/*
 * Copyright 2009 MBARI
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1 
 * (the "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package moos.ssds.data.parsers;

import java.util.Map;

import moos.ssds.metadata.RecordDescription;

/**
 * <p>
 * TODO kgomes This needs to be implemented
 * </p>
 * <hr>
 * 
 * @author : $Author: kgomes $
 * @version : $Revision: 1.1.2.2 $
 * @stereotype thing
 */
public class AsciiFixedWidthParser extends RecordParser {

    public AsciiFixedWidthParser(RecordDescription recordDescription) {
        super(recordDescription);
    }

    public Map parse(byte[] buffer) throws ParsingException {
        return null;
    }

}