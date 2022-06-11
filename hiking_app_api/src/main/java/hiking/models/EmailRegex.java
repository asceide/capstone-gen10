package hiking.models;

public enum EmailRegex {
    //Set the value of the Enum to the constant regex pattern
    EMAIL_REGEX(Constants.REGEX);

    // Stregthen the coupling between the Enum and Constant value by
    // Enforcing the correlation between the enum name and the constant
    // Found at https://stackoverflow.com/questions/13253624/how-to-supply-enum-value-to-an-annotation-from-a-constant-in-java/16384334#16384334
    EmailRegex(String regex) {
       if(!regex.equals(this.name())){
              throw new IllegalArgumentException("Regex must match enum name");
       }
    }

  // For use with Annotations, which require an enum or a constant for the value of its fields
  // So we get the regex needed to validate an email as constant
  // Regex found @ http://emailregex.com/
  public static class Constants {
      public static final String REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`"
          +"{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"("
              +"?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-" +
              "\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@" +
              "(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:" +
              "[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?" +
              "[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|" +
              "[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-" +
              "\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
  }
}
