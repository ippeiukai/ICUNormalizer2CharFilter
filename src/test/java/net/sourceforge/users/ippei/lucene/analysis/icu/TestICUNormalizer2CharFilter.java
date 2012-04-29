/**
 * ICUNormalizer2CharFilter
 * Copyright 2010-2012 Ippei Ukai
 */
package net.sourceforge.users.ippei.lucene.analysis.icu;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.CharStream;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.junit.Test;

import com.ibm.icu.text.Normalizer2;

public class TestICUNormalizer2CharFilter extends BaseTokenStreamTestCase {
  
  @Test
  public void testNormalization() throws IOException {
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
  
  @Test
  public void testTokenStream() throws IOException {
    // '℃', '№', '㈱', '㌘', 'ｻ'+'<<', 'ｿ'+'<<', '㌰'+'<<'
    String input = "℃ № ㈱ ㌘ ｻﾞ ｿﾞ ㌰ﾞ";

    CharStream reader = new ICUNormalizer2CharFilter(new StringReader(input),
        ICUNormalizer2CharFilter.Form.NFKC);

    TokenStream tokenStream = new MockTokenizer(reader, MockTokenizer.WHITESPACE, false);
    
    assertTokenStreamContents(tokenStream, 
        new String[] {"°C", "No", "(株)", "グラム", "ザ", "ゾ", "ピゴ"}, 
        new int[] {0, 2, 4, 6, 8, 11, 14}, 
        new int[] {1, 3, 5, 7, 10, 13, 16},
        input.length());
  }
  
  @Test
  public void testTokenStream2() throws IOException {
    // '㌰', '<<'゙, '5', '℃', '№', '㈱', '㌘', 'ｻ', '<<', 'ｿ', '<<'
    String input = "㌰゙5℃№㈱㌘ｻﾞｿﾞ";

    CharStream reader = new ICUNormalizer2CharFilter(new StringReader(input),
        ICUNormalizer2CharFilter.Form.NFKC_CF);

    TokenStream tokenStream = new NGramTokenizer(reader, 1,1);
    
    assertTokenStreamContents(tokenStream,
        new String[] {"ピ", "ゴ", "5", "°", "c", "n", "o", "(", "株", ")", "グ", "ラ", "ム", "ザ", "ゾ"},
        new int[]{0, 1, 2, 3, 3, 4, 4, 5, 5, 5, 6, 6, 6, 7, 9},
        new int[]{1, 2, 3, 3, 4, 4, 5, 5, 5, 6, 6, 6, 7, 9, 11},
        input.length()
      );
  }
  
}
