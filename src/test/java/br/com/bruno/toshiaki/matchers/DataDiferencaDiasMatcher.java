package br.com.bruno.toshiaki.matchers;

import br.com.bruno.toshiaki.utils.DataUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

  private final Integer qtdDias;

  public DataDiferencaDiasMatcher(final Integer qtdDias) {
    this.qtdDias = qtdDias;
  }

  @Override
  public void describeTo(final Description desc) {
    final var dataEsperada = DataUtils.obterDataComDiferencaDias(this.qtdDias);
    final var format = new SimpleDateFormat("dd/MM/yyyy");
    desc.appendText(format.format(dataEsperada));
  }

  @Override
  protected boolean matchesSafely(final Date data) {
    return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(this.qtdDias));
  }

}
