package net.centilehcf.core.rank.comparator;

import net.centilehcf.core.rank.Rank;
import java.util.Comparator;

public class RankComparator implements Comparator<Rank> {

	@Override
	public int compare(Rank rank1, Rank rank2) {
		return rank2.getWeight() - rank1.getWeight();
	}

}
