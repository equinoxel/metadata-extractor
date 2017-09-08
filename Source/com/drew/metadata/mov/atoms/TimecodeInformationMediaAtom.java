/*
 * Copyright 2002-2017 Drew Noakes
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * More information about this project is available at:
 *
 *    https://drewnoakes.com/code/exif/
 *    https://github.com/drewnoakes/metadata-extractor
 */
package com.drew.metadata.mov.atoms;

import com.drew.lang.SequentialReader;
import com.drew.metadata.mov.media.QtTimecodeDirectory;

import java.io.IOException;

/**
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap3/qtff3.html#//apple_ref/doc/uid/TP40000939-CH205-BBCBEAGJ
 *
 * @author Payton Garland
 */
public class TimecodeInformationMediaAtom extends FullAtom
{
    int textFont;
    int textFace;
    int textSize;
    int[] textColor;
    int[] backgroundColor;
    String fontName;

    public TimecodeInformationMediaAtom(SequentialReader reader, Atom atom) throws IOException
    {
        super(reader, atom);

        textFont = reader.getInt16();
        textFace = reader.getInt16();
        textSize = reader.getInt16();
        reader.skip(2); // Reserved
        textColor = new int[]{reader.getUInt16(), reader.getUInt16(), reader.getUInt16()};
        backgroundColor = new int[]{reader.getUInt16(), reader.getUInt16(), reader.getUInt16()};
        fontName = reader.getString(reader.getUInt8());
    }

    public void addMetadata(QtTimecodeDirectory directory)
    {
        directory.setInt(QtTimecodeDirectory.TAG_TEXT_FONT, textFont);
        switch (textFace) {
            case (0x0001):
                directory.setString(QtTimecodeDirectory.TAG_TEXT_FACE, "Bold");
                break;
            case (0x0002):
                directory.setString(QtTimecodeDirectory.TAG_TEXT_FACE, "Italic");
                break;
            case (0x0004):
                directory.setString(QtTimecodeDirectory.TAG_TEXT_FACE, "Underline");
                break;
            case (0x0008):
                directory.setString(QtTimecodeDirectory.TAG_TEXT_FACE, "Outline");
                break;
            case (0x0010):
                directory.setString(QtTimecodeDirectory.TAG_TEXT_FACE, "Shadow");
                break;
            case (0x0020):
                directory.setString(QtTimecodeDirectory.TAG_TEXT_FACE, "Condense");
                break;
            case (0x0040):
                directory.setString(QtTimecodeDirectory.TAG_TEXT_FACE, "Extend");
        }

        directory.setInt(QtTimecodeDirectory.TAG_TEXT_SIZE, textSize);
        directory.setIntArray(QtTimecodeDirectory.TAG_TEXT_COLOR, textColor);
        directory.setIntArray(QtTimecodeDirectory.TAG_BACKGROUND_COLOR, backgroundColor);
        directory.setString(QtTimecodeDirectory.TAG_FONT_NAME, fontName);
    }
}