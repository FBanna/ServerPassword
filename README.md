# Server Password

Aims to solve the problem for large community servers in which whitelisting many people becomes cumbersome.

Simply share a password to members who want to join, when they connect to the server for the first time they are greeted with a password screen (This is during the configuration phase). If they enter the password correctly they are added to the whitelist and will not be shown it again.

The password can be configured in the config but by default is: `change_me`.

The message shown to people logging in can be changed in the config.

Ensure that `enforce-whitelist` (in server.properties) and whitelist are both enabled.

The mod uses dialogs to prompt the user for passwords. It should also work for geyser players.