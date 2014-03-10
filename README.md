The Hand
========

A serializable rules engine.

When you use this library, you - the developer - are king (or queen). The king defines the rules; the Hand of the King
writes down the king's rules and enforces them.

## Publishing

This project is published to Maven Central.


## Contributors

If you would like to contribute to this project and would like to be able to publish new versions, you will need the
following (more in-depth instructions at http://www.scala-sbt.org/release/docs/Community/Using-Sonatype.html):

1. Generate a GPG key pair
    1. Download GPG tools from http://gpgtools.org/
    2. Run `gpg --gen-key`
2. Create a Sonatype JIRA account
    1. https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-2.Signup
    2. Contact us to associate your account with this repository
3. Add your Sonatype JIRA credentials to ~/.sbt/sonatype.sbt

        credentials += Credentials("Sonatype Nexus Repository Manager",
                                   "oss.sonatype.org",
                                   "your-sonatype-username",
                                   "your-sonatype-password")

4. Run `sbt publish-signed`