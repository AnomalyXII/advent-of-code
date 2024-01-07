package net.anomalyxii.aoc;

import java.util.Arrays;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link SolutionLoader} that uses an {@link ServiceLoader SPI} to find
 * {@link Challenge Challenges}.
 */
public class SpiSolutionLoader implements SolutionLoader {

    private static final ServiceLoader<Solutions> SERVICE_LOADER = ServiceLoader.load(Solutions.class);

    // ****************************************
    // SolutionLoader Methods
    // ****************************************

    @Override
    public Set<Challenge<?, ?>> allChallenges() {
        return SERVICE_LOADER.stream()
                .map(ServiceLoader.Provider::get)
                .map(Solutions::allChallenges)
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Challenge<?, ?>> allChallengesForYear(final int year) {
        return SERVICE_LOADER.stream()
                .map(ServiceLoader.Provider::get)
                .map(Solutions::allChallenges)
                .flatMap(Arrays::stream)
                .filter(c -> c.matches(year, null))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Challenge<?, ?>> findChallenge(final int year, final int day) {
        return SERVICE_LOADER.stream()
                .map(ServiceLoader.Provider::get)
                .map(Solutions::allChallenges)
                .flatMap(Arrays::stream)
                .filter(c -> c.matches(year, day))
                .findFirst();
    }

}
