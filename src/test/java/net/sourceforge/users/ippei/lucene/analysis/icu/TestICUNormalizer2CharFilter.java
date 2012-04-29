/**
 * ICUNormalizer2CharFilter
 * Copyright 2010-2012 Ippei Ukai
 */
package net.sourceforge.users.ippei.lucene.analysis.icu;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.CharStream;
import org.junit.Test;

import com.ibm.icu.text.Normalizer2;

public class TestICUNormalizer2CharFilter extends BaseTokenStreamTestCase {
  
  @Test
  public void test() throws IOException {
    String input = "ʰ㌰゙5℃№㈱㌘，バッファーの正規化のテスト．㋐㋑㋒㋓㋔ｶｷｸｹｺｻﾞｼﾞｽﾞｾﾞｿﾞg̈각/각நிเกषिchkʷक्षि";
    Normalizer2 referenceNormalizer = Normalizer2.getInstance(null, "nfkc_cf",
        Normalizer2.Mode.COMPOSE);
    String expectedOutput = referenceNormalizer.normalize(input);
    
    CharStream reader = new ICUNormalizer2CharFilter(new StringReader(input),
        ICUNormalizer2CharFilter.Form.NFKC_CF);
    char[] tempBuff = new char[10];
    StringBuilder output = new StringBuilder();
    while (true) {
      int length = reader.read(tempBuff);
      if (length == -1) {
        break;
      }
      output.append(tempBuff, 0, length);
      assertEquals(
          output.toString(),
          referenceNormalizer.normalize(input.substring(0,
              reader.correctOffset(output.length()))));
    }
    
    assertEquals(expectedOutput, output.toString());
  }
  
}
