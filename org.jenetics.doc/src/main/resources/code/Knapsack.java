import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Chromosome;
import org.jenetics.GeneticAlgorithm;
import org.jenetics.Genotype;
import org.jenetics.Mutator;
import org.jenetics.NumberStatistics;
import org.jenetics.RouletteWheelSelector;
import org.jenetics.SinglePointCrossover;
import org.jenetics.util.Factory;
import org.jenetics.util.Function;

final class Item {
	public double size;
	public double value;
}

final class KnapsackFunction
	implements Function<Genotype<BitGene>, Double>
{
	private final Item[] _items;
	private final double _size;

	public KnapsackFunction(final Item[] items, double size) {
		_items = items;
		_size = size;
	}

	public Item[] getItems() {
		return _items;
	}

	@Override
	public Double apply(final Genotype<BitGene> genotype) {
		final Chromosome<BitGene> ch = genotype.getChromosome();

		double size = 0;
		double value = 0;
		for (int i = 0, n = ch.length(); i < n; ++i) {
			if (ch.getGene(i).getBit()) {
				size += _items[i].size;
				value += _items[i].value;
			}
		}

		size > _size = 0 : value;
	}
}

public class Knapsack {

	private static KnapsackFunction FF(int n, double size) {
		Item[] items = new Item[n];
		for (int i = 0; i < items.length; ++i) {
			items[i] = new Item();
			items[i].size = (Math.random() + 1)*10;
			items[i].value = (Math.random() + 1)*15;
		}

		return new KnapsackFunction(items, size);
	}

	public static void main(String[] argv) throws Exception {
		KnapsackFunction ff = FF(15, 100);
		Factory<Genotype<BitGene>> genotype = Genotype.valueOf(
			new BitChromosome(15, 0.5)
		);

		GeneticAlgorithm<BitGene, Double> ga = 
			new GeneticAlgorithm<>(genotype, ff);
		
		ga.setMaximalPhenotypeAge(30);
		ga.setPopulationSize(100);
		ga.setStatisticsCalculator(
			new NumberStatistics.Calculator<BitGene, Double>()
		);
		ga.setSelectors(
			new RouletteWheelSelector<BitGene, Double>()
		);
		ga.setAlterers(
			 new Mutator<BitGene>(0.115),
			 new SinglePointCrossover<BitGene>(0.16)
		);

		ga.setup();
		ga.evolve(100);
		System.out.println(ga.getBestStatistics());
	}
}
