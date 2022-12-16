package br.com.bruno.toshiaki.suites;

import br.com.bruno.toshiaki.servicos.CalculoValorLocacaoTest;
import br.com.bruno.toshiaki.servicos.LocacaoServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({CalculoValorLocacaoTest.class,
    LocacaoServiceTest.class})
public class SuiteExecucao {

}
