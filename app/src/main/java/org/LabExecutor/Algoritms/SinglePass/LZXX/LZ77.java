package org.LabExecutor.Algoritms.SinglePass.LZXX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LZ77 Compression/Decompression algorithms.
 * @see <a href="https://github.com/TheAlgorithms/Python/blob/master/compression/lz77.py">Python implementation</a>
 * @author evosome
 */
class LZ77 {

    private final int windowSize;
    private final int bufferSize;

    public LZ77(int windowSize, int bufferSize) {
        this.windowSize = windowSize;
        this.bufferSize = bufferSize;
    }

    public EncodingResult encode(String line) {

        EncodingResult result = new EncodingResult();

        //TODO: create LZCharBuffer class, representing character buffer instead using this
        char[] chars = new char[windowSize];
        Arrays.fill(chars, '\00');
        String searchBuffer = String.copyValueOf(chars);

        while (!line.isEmpty()) {
            Token token = matchToken(line, searchBuffer, windowSize);

            result.addDictRow(searchBuffer);
            result.addBufferRow(line.substring(0, Math.min(line.length(), bufferSize)));
            result.addToken(token);

            searchBuffer += line.substring(0, token.getLength() + 1);
            if (searchBuffer.length() > windowSize) {
                searchBuffer = searchBuffer.substring(searchBuffer.length() - windowSize);
            }

            line = line.substring(token.getLength() + 1);
        }

        return result;
    }

    private Token matchToken(String text, String searchBuff, int searchBuffSize) {
        int length = 0, offset = 0, sbSize = searchBuff.length();

        if (!searchBuff.isEmpty() && text.length() > 1) {

            for (int i = 0; i < sbSize; i++) {
                if (searchBuff.charAt(i) != text.charAt(0))
                    continue;

                int foundLength = matchMaxLengthOf(text, searchBuff, 0, i);
                if (foundLength > length) {
                    offset = i;
                    length = foundLength;
                }
            }

        }

        return new Token(offset, length, text.charAt(length));
    }

    private int matchMaxLengthOf(
            String text,
            String window,
            int textIndex,
            int windowIndex) {

        if (text.length() <= textIndex || bufferSize - 1 <= textIndex || window.length() <= windowIndex)
            return 0;

        if (text.isEmpty() || text.charAt(textIndex) != window.charAt(windowIndex))
            return 0;

        return 1 + matchMaxLengthOf(
                text,
                window + text.charAt(textIndex),
                textIndex + 1,
                windowIndex + 1);

    }

    public class EncodingResult {
        private final List<String> dictRows;
        private final List<String> bufferRows;
        private final List<Token> tokens;

        public EncodingResult() {
            dictRows = new ArrayList<>();
            bufferRows = new ArrayList<>();
            tokens = new ArrayList<>();
        }

        public void addDictRow(String row) { dictRows.add(row); }
        public void addBufferRow(String row) { bufferRows.add(row); }
        public void addToken(Token token) { tokens.add(token); }

        public String[] getBufferRows() { return bufferRows.toArray(String[]::new); }
        public String[] getDictRows() { return dictRows.toArray(String[]::new); }
        public String[] getTokensAsString() {
            return tokens
                    .stream()
                    .map(Token::toString)
                    .toArray(String[]::new);
        }
    }

    public class Token {

        private final int offset;
        private final int length;
        private final char indicator;

        public Token(int offset, int length, char indicator) {
            this.offset = offset;
            this.length = length;
            this.indicator = indicator;
        }

        @Override
        public String toString() { return String.format("<%d,%d,%s>", offset, length, indicator); }

        public int getOffset() { return offset; }

        public int getLength() { return length; }

        public char getIndicator() { return indicator; }
    }

}