package org.LabExecutor.Algoritms.SinglePass.LZXX;

public class LZXXAlgorithms {

    public static LZXXReport encode(String line) {
        LZ77 lz77 = new LZ77(10, 6);

        LZ77.EncodingResult lz77result = lz77.encode(line);

        return new LZXXReport(
                new LZ77Report(
                        lz77result.getDictRows(),
                        lz77result.getBufferRows(),
                        lz77result.getTokensAsString())
        );
    }

    public static DecodeReport decode(String line) {

        return new DecodeReport(line, "001001010010");
    }

    /**
     * Represents report for the LZ77 algorithm
     * @param dictRows Dictionary rows at each encoding step
     * @param bufferRows Buffer rows at each encoding step
     * @param tokens Result tokens for each encoding step
     */
    static record LZ77Report(
            String[] dictRows,
            String[] bufferRows,
            String[] tokens) {
    }

    /**
     * Represents all reports from LZ algorithms
     * @param lz77 LZ77 report
     */
    static record LZXXReport(
            LZ77Report lz77) {
    }

    static record DecodeReport(
            String input_line,
            String result) {
    }
}
