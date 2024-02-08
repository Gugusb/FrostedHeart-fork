package com.teammoeg.frostedheart.scenario.runner;

import com.teammoeg.frostedheart.scenario.parser.Scenario;

public interface IScenarioThread {
	void setScenario(Scenario s);
	Scenario getScenario();
	void setNodeNum(int num);
	int getNodeNum();
	String getLang();
	void sendMessage(String s);
}
