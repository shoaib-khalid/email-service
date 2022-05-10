##################################################
# email-3.0.15-SNAPSHOT | 10-May-2022
##################################################

Update template footer.html


##################################################
# email-3.0.14-SNAPSHOT | 13-April-2022
##################################################

### Code Changes:
add staging domain : dev-my & dev-pk in code for logo path


##################################################
# email-3.0.13-SNAPSHOT | 05-April-2022
##################################################
### Code Changes:
Bug fix for configuration in logo url with 3 different url :

@Value("${symplified.logo.path:https://symplified.biz/store-assets/symplified-logo-small.png}")
private String symplifiedLogoPath;

@Value("${deliverin.logo.path:https://symplified.biz/store-assets/deliverin-logo-small.png}")
private String deliverinLogoPath;

@Value("${easydukan.logo.path:https://symplified.biz/store-assets/easydukan-logo-small.png}")
private String easydukanLogoPath;


##################################################
# email-3.0.12-SNAPSHOT | 01-April-2022
##################################################
### Code Changes:
Default logo will be based on domain passed to email-service


##################################################
# email-3.0.11-SNAPSHOT | 30-March-2022
##################################################
### Code Changes:
Add new request parameter setFromName to set Sender Name


##################################################
# email-3.0.10-SNAPSHOT | 24-March-2022
##################################################
### Code Changes:
For email  /no-reply/user-account, logo will load based on domain passed in


##################################################
# email-3.0.9-SNAPSHOT | 24-Feb-2022
##################################################
### Code Changes:
Set Symplified logo path to https://symplified.biz/store-assets/symplified-logo-small.png 


##################################################
# email-3.0.8-SNAPSHOT | 24-Feb-2022
##################################################
### Code Changes:
New template for reset-password


##################################################
# email-3.0.7-SNAPSHOT | 04-Jan-2022
##################################################
### Code Changes:
Add request parameter 'from' to allow override from address by client


##################################################
# email-3.0.6-SNAPSHOT | 31-Dec-2021
##################################################
### Code Changes:
Add new OrderStatus enum CANCELED_BY_MERCHANT


##################################################
# email-3.0.4-SNAPSHOT | 10-October-2021
##################################################
### Code Changes:
* Changed version from 3.0.3 to 3.0.4
* added following lines in SessionRequestFilter
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	

### Properties Changes:
no changes
