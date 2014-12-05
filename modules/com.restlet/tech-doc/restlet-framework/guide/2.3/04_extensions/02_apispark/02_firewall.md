# Firewall

## Introduction

This service integrated in the [APISpark extension](../) adds rate limitation to your web API.

## Configuration

### Using Maven

Add the following dependency in your pom.xml : 

~~~~{.java}
	<dependency>
		<groupId>org.restlet.jse</groupId>
		<artifactId>org.restlet.ext.apispark</artifactId>
		<version>2.3-SNAPSHOT</version>
	</dependency>
~~~~

### Manually 

You must add the following jars (provided in 
[Restlet Framework](http://restlet.com/downloads/current#release=unstable&edition=jse&distribution=zip) in the "/path/to/your/lib" folder or manually to the classpath.

* 	org.restlet.ext.apispark.jar (Restlet APISpark extension with firewall)
*	com.google.guava_16.0
*	javax.ws.rs_2.0

## Usage example

To use the firewall, first create an instance of FirewallFilter

~~~~{.java}
	FirewallFilter firewallFilter = new FirewallFilter();
~~~~

Then, on your firewall you can add multiple rules. For now, these rules are rate limiters.
There are two kinds of rate limiters :   

* Concurrent rate limiter : Limits the number of concurrent requests.
* Periodic rate limiter : Limits the number of requests on a given period.

There are different factories to create a Firewall rule (located in class FirewallUtils).

* Create a periodic rate limiter, with users identified by their identifier (after authentication), a limit depending on their role and a default limit (for non authenticated users or for users without a role)  

~~~~{.java}
	// Create a map which will associate roles with limits.  
	Map<String, Integer> limitsPerRole = new HashMap<String, Integer>();

	// Associate the role "owner" to a limit of 100 requests.  
	limitsPerRole.put("owner", 100);

	// Associate the role "user" to a limit of 10 requests.  
	limitsPerRole.put("user", 10);

	// Create a periodic rate limitation rule with a period of 60 seconds, the defined limits per role and a default limit of 5 requests.  
	FirewallUtils.addRolesPeriodicCounter(firewallFilter, 60, limitsPerRole, 5);
~~~~  

* Create a periodic rate limiter, with users identified by their identifier (after authentication), a limit depending on their role and without a default limit (set by default at O) 
   
~~~~{.java}
	// Create a period rate limitation rule with a period of 60 seconds, the defined limits per role (defined in the example above).  
	FirewallUtils.addRolesPeriodicCounter(firewallFilter, 60, limitsPerRole, 5);
~~~~
  
* Create a periodic rate limiter, with users identified by their IP address. Same limit for all.  
    
~~~~{.java}
	// Create a periodic rate limitation rule with a period of 60 seconds and a limit of 10 calls per IP address.
	FirewallUtils.addIpAddressesPeriodicCounter(firewallFilter, 60, 10)
~~~~  

* Create a concurrent rate limiter, with users identified by their identifier (after authentication) and with a limit depending on their role and a default limit (for non authenticated users or for users without a role)  

~~~~{.java}
	// Create a map which will associate roles with limits.  
	Map<String, Integer> limitsPerRole = new HashMap<String, Integer>();

	// Associate the role "owner" to a limit of 10 requests.  
	limitsPerRole.put("owner", 10);

	// Associate the role "user" to a limit of 5 requests.  
	limitsPerRole.put("user", 5);
	
	// Create a period rate limitation rule the defined limits per role and a default limit of 2 requests.  
	FirewallUtils.addRolesConcurrencyCounter(firewallFilter, limitsPerRole, 2);
~~~~

* Create a concurrent rate limiter, with users identified by their identifier (after authentication) and with a limit depending on their role and without a default limit (set by default at O)  

~~~~{.java}
	// Create a period rate limitation rule the defined limits per role.  
	FirewallUtils.addRolesConcurrencyCounter(firewallFilter, limitsPerRole);
~~~~

* Create a concurrent rate limiter, with users identified by their IP address. Same limit for all.
	
~~~~{.java}
	// Create a concurrent rate limitation rule with a limit of 10 calls per IP address.
	FirewallUtils.addIpAddressesConcurrentCounter(firewallFilter, 10)
~~~~

## Go further

It is also possible to customize the use of the Rate Limitation rules.

A Rate limitation rule is associated to several objects :   

* A CountingPolicy which gives an identifier to the request. Here are the existing Counting Policies (it's also possible to implement your own CountingPolicy):  
    * IpAddressCountingPolicy : The identifier is the IP Address of the client.  
    * UserCountingPolicy : The identifier is the identifier of the client (given by the ChallengeAuthenticator).  

* A (or several) ThresholdHandler which make a action when a limit is reached.   
  * A ThresholdHandler owns a LimitPolicy. This LimitPolicy associates an identifier (defined in the CountingPolicy) to a limit. Here are the existing Limit Policies (it's also possible to implement your own LimitPolicy):
    * RoleLimitPolicy : Associates the role of the client to a limit.
    * UniqueLimitPolicy : Attributes the same Limit Policy for all requests.
  * There are also different implementation of ThresholdHandler : 
    * BlockingHandler : Returns a 429 response (Too many requests) when the limit is reached. 
    * RateLimitationHandler : Returns a 429 response (Too many requests) when the limit is reached and adds some "Rate Limitation headers" :

~~~~
	X-RateLimit-Limit : 500 # Number of requests authorized for a period
    X-RateLimit-Remaining : 289 # Number of remaining requests authorized for the current period
    X-RateLimit-Reset : 123456789 # When the period will reset
~~~~

### Example 

* Create a Rate Limiter which counts 
    * IP Addresses
    * On a 1 minute period
    * With a unique limit : 10 calls
    * With a RateLimitationHandler (to return the "Rate limitationHeaders")

~~~~{.java}
	// Create the Firewall Filter
	FirewallFilter firewallFiler = new FirewallFilter();
	
	// Add a periodic rule with a period of 1 minute (60 seconds) and a IPCountingPolicy (to count the clients IP addresses) 
	FirewallCounterRule rule = new PeriodicFirewallCounterRule(60, new IpCountingPolicy());

	// Attach an RateLimitationHandler (with a UniqueLimitPolicy) to the FirewallRule
	rule.addHandler(new RateLimitationHandler(new UniqueLimitPolicy(10));

	// Attach the rule to the FirewallFilter
	firewallFilter.addCounter(rule);
~~~~
