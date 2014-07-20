class { 'java':
  distribution => 'jdk',
  version      => 'latest',
}
include java

class { 'postgresql::server': }

postgresql::server::db { 'mydatabasename':
  user     => 'sa',
  password => postgresql_password('sa', '123'),
}


class soft {
  package { 'Git is istalled':
    name   => 'git',
    ensure => latest
  }

  package { 'The Midnight commander installation':
    name   => 'git',
    ensure => latest
  }
}

include soft
