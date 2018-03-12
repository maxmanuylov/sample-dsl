agentFor("http://my.server:8111") {
    name = "My agent ($osName)"

    config {
        + "param1" to "a"
        + "param2" to "b"
        + "param3" to "c"
    }

    system {
        + "sys1" to "11"
        + "sys2" to "22"
    }

    env {
        + "ENV1" to "VALUE1"
        + "ENV2" to "VALUE2"
    }
}
