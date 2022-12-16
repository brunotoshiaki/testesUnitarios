package br.com.bruno.toshiaki.matchers;

import java.util.Calendar;

public class MatchersProprios {


	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
}
