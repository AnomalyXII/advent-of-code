#
# Module Definitions
#

load("@rules_java//java:defs.bzl", "java_library", "java_binary", "java_plugin")
load("@rules_jvm_external//:defs.bzl", _artifact = "artifact")
load("@contrib_rules_jvm//java:defs.bzl", "java_test_suite", "java_junit5_test", "checkstyle_test", "spotbugs_test")
load("@bazel_skylib//rules:build_test.bzl", "build_test")

def artifact(dep):
    return _artifact(dep, repository_name = "maven")

def aoc_solutions(name, **kwargs):
    """Main `java` solutions library + supporting bundles"""

    native.filegroup(
        name = "%s/inputs" % name,
        srcs = kwargs.pop("resources", native.glob(["src/main/resources/**"])),
        visibility = kwargs.get("visibility", ["//:__subpackages__"]),
    )

    aoc_library(name = name, **kwargs)

def aoc_library(name, **kwargs):
    """Main `java` library"""

    srcs = kwargs.pop("srcs", native.glob(["src/main/java/**/*.java"]))
    resources = kwargs.pop("resources", native.glob(["src/main/resources/**"]))

    java_library(
        name = name,
        srcs = srcs,
        resources = resources,
        visibility = kwargs.pop("visibility", ["//:__subpackages__"]),
        **kwargs
    )

    checkstyle_test(
        name = "%s-checkstyle" % name,
        srcs = srcs,
        config = "//build:checkstyle-config",
        tags = ["build"],
    )

    spotbugs_test(
        name = "%s-spotbugs" % name,
        deps = [":%s" % name],
        config = "//build:spotbugs-config",
        timeout="short",
        tags = ["build"],
    )

    build_test(
        name = "%s-build-test" % name,
        targets = [
            ":%s" % name,
        ],
        tags = ["build"],
    )

def aoc_binary(name, **kwargs):
    """Main `java` binary"""

    srcs = kwargs.pop("srcs", native.glob(["src/main/java/**/*.java"]))
    resources = kwargs.pop("resources", native.glob(["src/main/resources/**"]))

    java_binary(
        name = name,
        srcs = srcs,
        resources = resources,
        visibility = kwargs.pop("visibility", ["//:__subpackages__"]),
        **kwargs
    )

    checkstyle_test(
        name = "%s-checkstyle" % name,
        srcs = srcs,
        config = "//build:checkstyle-config",
        tags = ["build"],
    )

    spotbugs_test(
        name = "%s-spotbugs" % name,
        deps = [":%s" % name],
        config = "//build:spotbugs-config",
        timeout="short",
        tags = ["build"],
    )

    build_test(
        name = "%s-build-test" % name,
        targets = [
            ":%s" % name,
        ],
        tags = ["build"],
    )

def aoc_plugin(name, **kwargs):
    """Compiler plug-in for a `java` library"""

    srcs = kwargs.pop("srcs", native.glob(["src/main/java/**/*.java"]))
    resources = kwargs.pop("resources", native.glob(["src/main/resources/**"]))

    java_plugin(
        name = name,
        srcs = srcs,
        resources = resources,
        visibility = kwargs.pop("visibility", ["//:__subpackages__"]),
        **kwargs
    )

    checkstyle_test(
        name = "%s-checkstyle" % name,
        srcs = srcs,
        config = "//build:checkstyle-config",
        tags = ["build"],
    )

    build_test(
        name = "%s-build-test" % name,
        targets = [
            ":%s" % name,
        ],
        tags = ["build"],
    )

def aoc_test_suite(name, **kwargs):
    """Test suite of unit tests"""

    srcs = kwargs.pop("srcs", native.glob(["src/test/java/**/*.java"]))
    resources = kwargs.pop("resources", native.glob(["src/test/resources/**"]))

    java_test_suite(
        name = name,
        srcs = srcs,
        resources = resources,
        runner = kwargs.pop("runner", "junit5"),
        size = kwargs.pop("size", "small"),
        tags = kwargs.pop("tags", ["smoke"]),
        **kwargs
    )

    checkstyle_test(
        name = "%s-checkstyle" % name,
        srcs = srcs,
        config = "//build:checkstyle-config",
        tags = ["build"],
    )

    build_test(
        name = "%s-build-test" % name,
        targets = [
            ":%s" % name,
        ],
        tags = ["build"],
    )