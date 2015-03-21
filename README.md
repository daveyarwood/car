# car-stuff

A little CLI tool that, when prompted, will tell me the next mile markers when things are due for my car, e.g. oil changes and tire rotations.

## Setup

Copy the example `config.edn` to `~/.config/car-stuff/config.edn`:

    $ cp config.edn $HOME/.config/car-stuff/config.edn

Edit `~/.config/car-stuff/config.edn` to your liking.

Make `car-stuff` executable and copy it into `/usr/bin` or whatever:

    $ chmod +x car-stuff
    $ cp car-stuff /usr/bin

## Usage

The first time you run it, it will ask you for an odometer reading so it can figure out when stuff is due. You could also put this in your `config.edn` as the value for the key `:current`. Or you could do this:

    $ car-stuff current 100000
    Current mileage: 100000

(which just stores `:current 100000` in your `config.edn`)

To find out what value is stored as your current mileage at any time, do this:

    $ car-stuff current
    Current mileage: 100000

Tell car-stuff the name of a task and it'll tell you when it's due next. Or, just type `car-stuff` on its lonesome and it'll tell you when each of the things are due next.

    $ car-stuff
    Oil change: due at 105000
    Rotate tires: due at 102000

    $ car-stuff oil-change
    Oil change: due at 105000

    $ car-stuff 'ROTATE TIRES'
    Rotate tires: due at 102000

Oh, but wait. I'm past 102,000 and 105,000 now. It's been a really long time and I forgot to update my current mileage, I'm actually at 260,000 miles now or something ridiculous. wut do i do?

    $ car-stuff current 260000
    Current mileage: 260000

    $ car-stuff
    Oil change: due at 262500
    Rotate tires: due at 264000

Bingo bango bongo.
