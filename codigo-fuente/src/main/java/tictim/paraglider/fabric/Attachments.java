package tictim.paraglider.fabric;
public final class Attachments {
 public static <T> T get(Object holder,AttachmentType<T> type){return type.get(holder);}
 public static <T> void set(Object holder,AttachmentType<T> type,T value){type.set(holder,value);}
}
