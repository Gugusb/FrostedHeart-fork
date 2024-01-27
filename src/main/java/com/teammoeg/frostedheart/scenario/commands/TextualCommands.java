package com.teammoeg.frostedheart.scenario.commands;

import java.util.HashMap;
import java.util.Map;

import com.teammoeg.frostedheart.scenario.FHScenario;
import com.teammoeg.frostedheart.scenario.Param;
import com.teammoeg.frostedheart.scenario.runner.RunStatus;
import com.teammoeg.frostedheart.scenario.runner.ScenarioConductor;

public class TextualCommands {
	public void nowait(ScenarioConductor runner) {
		runner.sendCachedSence();
		runner.getScene().isNowait=true;
	}
	public void endnowait(ScenarioConductor runner) {
		runner.sendCachedSence();
		runner.getScene().isNowait=false;
	}
	public void r(ScenarioConductor runner) {
		runner.newLine();
	}
	public void link(ScenarioConductor runner,@Param("lid")String linkId,@Param("s")String scenario,@Param("l")String label) {
		runner.sendCachedSence();
		linkId=runner.createLink(linkId, scenario, label);
		Map<String,String> pars=new HashMap<>();
		pars.put("lid", linkId);
		FHScenario.callClientCommand("link", runner, pars);
	}
	public void endlink(ScenarioConductor runner) {
		runner.sendCachedSence();
		Map<String,String> pars=new HashMap<>();
		FHScenario.callClientCommand("endlink", runner, pars);
	}
	public void nolink(ScenarioConductor runner) {
		runner.clearLink();
	}
	public void delay(ScenarioConductor runner,@Param("t")int t) {
		runner.getScene().addWait(t);
		runner.sendCachedSence();
	}
	public void er(ScenarioConductor runner) {
		runner.getScene().clear();
		runner.sendCachedSence();
	}
	public void l(ScenarioConductor runner) {
		runner.getScene().waitClient(true);
		runner.sendCachedSence();
	}
	public void wc(ScenarioConductor runner) {
		runner.getScene().waitClient(false);
		runner.sendCachedSence();
	}
	public void wt(ScenarioConductor runner) {
		runner.setStatus((RunStatus.WAITTRIGGER));
		runner.sendCachedSence();
	}
	public void wa(ScenarioConductor runner) {
		runner.setStatus((RunStatus.WAITACTION));
		runner.sendCachedSence();
	}
	public void s(ScenarioConductor runner) {
		runner.stop();
		runner.getScene().clear();
	}

}
