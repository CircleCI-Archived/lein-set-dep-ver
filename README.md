# lein-set-dep-ver

A Leiningen plugin to rewrite your project.clj, updating the version of a specific dep. Similar to 'bundle update', but only works on a single version.

## Usage

Put `[lein-set-dep-ver "0.1.0"]` into the `:plugins` vector of your project.clj.
then

    $ lein set-dep-ver org.clojure/clojure 1.6.0

## License

Copyright © 2014 Allen Rohner

Distributed under the Eclipse Public License either version 1.0
