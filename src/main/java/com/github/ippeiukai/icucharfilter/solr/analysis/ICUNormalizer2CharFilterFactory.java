/**
 * ICUNormalizer2CharFilter
 * Copyright 2012 Ippei Ukai
 */
package com.github.ippeiukai.icucharfilter.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.CharStream;
import org.apache.solr.analysis.BaseCharFilterFactory;

import com.github.ippeiukai.icucharfilter.lucene.analysis.icu.ICUNormalizer2CharFilter;

public class ICUNormalizer2CharFilterFactory extends BaseCharFilterFactory {
  
  private ICUNormalizer2CharFilter.Form form;
  
  @Override
  public void init(Map<String,String> args) {
    super.init(args);
    String formStr = args.get("form");
    if(formStr != null)
      form = ICUNormalizer2CharFilter.Form.valueOf(formStr);
  }
  
  @Override
  public CharStream create(CharStream in) {
    if (form == null)
      return new ICUNormalizer2CharFilter(in);
    else
      return new ICUNormalizer2CharFilter(in, form);
  }
  
}
