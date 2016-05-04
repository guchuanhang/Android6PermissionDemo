# Android6PermissionDemo
introduce how to apply dangerous permission in android6
To simulate how to apply dangerous permission in android6.0,there use system camera take picture.There we need apply read external 
storage dangerous permission read photo and display in screen.
  
  Then has a simply introduce.

  apply process is like below:
  
  1.by checkSelfPermission method to check app has granted permission,if granted,do operation directly.
  
  2.otherwise, if user override shouldShowRequestPermissionRationale method,if override,use this metod to info user why need this permission or those permissions. Then apply this permission or those permissions directly.
  
  3.otherwise,use requestPermissions method apply permssion directly.
  
  4.after user response,onRequestPermissionsResult will be called.This method is similar to startActivityForResult method.
