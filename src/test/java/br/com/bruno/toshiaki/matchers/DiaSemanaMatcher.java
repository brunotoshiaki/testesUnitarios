package br.com.bruno.toshiaki.matchers;


import br.com.bruno.toshiaki.utils.DataUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {

  private final Integer diaSemana;

  public DiaSemanaMatcher(Integer diaSemana) {
    this.diaSemana = diaSemana;
  }

  public void describeTo(Description desc) {
    var data = Calendar.getInstance();
    data.set(Calendar.DAY_OF_WEEK, diaSemana);
    var dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
        Locale.of("pt", "BR"));
    desc.appendText(dataExtenso);
  }

  @Override
  protected boolean matchesSafely(Date data) {
    return DataUtils.verificarDiaSemana(data, diaSemana);
  }

}
