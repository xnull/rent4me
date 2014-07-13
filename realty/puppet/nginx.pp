/**
 * http://krisjordan.com/essays/goodbye-server-configuration-woes-hello-puppet nginx installation and configuration
 */

package { "nginx":
  ensure => installed
}

service { "nginx":
  require => Package["nginx"],
  ensure  => running,
  enable  => true
}