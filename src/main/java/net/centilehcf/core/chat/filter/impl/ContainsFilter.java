package net.centilehcf.core.chat.filter.impl;

import net.centilehcf.core.Core;
import net.centilehcf.core.chat.filter.ChatFilter;

public class ContainsFilter extends ChatFilter {

	private final String phrase;

	public ContainsFilter(Core core, String phrase) {
		this(core, phrase, null);
	}

	public ContainsFilter(Core core, String phrase, String command) {
		super(core, command);
		this.phrase = phrase;
	}

	@Override
	public boolean isFiltered(String message, String[] words) {
		for (String word : words) {
			if (word.contains(this.phrase)) {
				return true;
			}
		}

		return false;
	}

}
