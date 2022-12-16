package br.com.bruno.toshiaki.matchers;

import br.com.bruno.toshiaki.utils.DataUtils;
import java.util.Date;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

	private final Integer qtdDias;

	public DataDiferencaDiasMatcher(Integer qtdDias) {
		this.qtdDias = qtdDias;
	}

	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(qtdDias));
	}

}
